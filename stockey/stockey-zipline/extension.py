# coding: UTF-8

from zipline.data.bundles import register


def anyquant_ingest(environ,
                    asset_db_writer,
                    minute_bar_writer,
                    day_bar_writer,
                    adjustment_writer,
                    calendar,
                    cache,
                    show_progress,
                    output_dir):
    import pandas as pd
    import numpy as np
    import pymysql as mysql
    import datetime

    host = '139.129.40.103'
    username = 'root'
    password = '9990'
    db = 'stockey'
    stock_info_table = 'stockinfopo'
    daily_data_table = 'dailydatapo'

    def load_stock_info():
        connection = mysql.connect(host=host,
                                   user=username,
                                   password=password,
                                   db=db,
                                   charset='utf8',
                                   cursorclass=mysql.cursors.DictCursor)

        result_list = []

        try:
            with connection.cursor() as cursor:
                query = 'select * from ' + stock_info_table
                cursor.execute(query)
                item = cursor.fetchone()
                while not (item is None):
                    result_list.append(item)
                    item = cursor.fetchone()
        finally:
            connection.close()

        return result_list

    def build_daily(symbol):
        connection = mysql.connect(host=host,
                                   user=username,
                                   password=password,
                                   db=db,
                                   charset='utf8',
                                   cursorclass=mysql.cursors.DictCursor)

        date_list = []
        item_list = []

        try:
            with connection.cursor() as cursor:
                # query = 'select * from ' + daily_data_table + " where ID like " + "'" + symbol + "2014%'"
                query = 'select * from ' + daily_data_table + " where ID > '" + symbol + "20120102'" + " and ID like " + "'" + symbol +"%'"
                cursor.execute(query)
                item = cursor.fetchone()
                while not (item is None):
                    item_list.append(item)
                    date_list.append(item['THEDATE'])
                    item = cursor.fetchone()
        finally:
            connection.close()

        data_frame = pd.DataFrame(index=date_list, columns=['open', 'high', 'low', 'close', 'volume'], dtype='float64')
        for i in range(0, len(date_list)):
            item = item_list[i]
            data_frame.ix[date_list[i]] = item['OPEN'], item['HIGH'], item['LOW'], item['CLOSE'], item['VOLUMN']

        return data_frame

    # asset_db_writer to write stock_info
    stock_info_list = load_stock_info()
    equities = pd.DataFrame(np.empty(len(stock_info_list), dtype=[
        ('symbol', 'object'),
        ('asset_name', 'object'),
        ('start_date', 'datetime64[ns]'),
        ('exchange', 'object')]))

    daily_data_list = []

    for sid in range(0, len(stock_info_list)):
        stock_info_dict = stock_info_list[sid]
        # write symbol
        equities.iloc[sid, 0] = stock_info_dict['SYMBOL']
        print('sid: {0}, symbol: {1}'.format(sid, stock_info_dict['SYMBOL']))
        # write name
        equities.iloc[sid, 1] = stock_info_dict['NAME']
        # write start_date
        start_time_str = stock_info_dict['INFO'].encode('UTF-8')
        start_time = datetime.datetime.strptime(start_time_str, '上市日期：%Y-%m-%d')
        equities.iloc[sid, 2] = start_time
        # write exchange
        equities.iloc[sid, 3] = stock_info_dict['MARKET']

        # fill in daily_data
        symbol = stock_info_dict['SYMBOL'][2:]
        daily_data = build_daily(symbol)
        box = (sid, daily_data)
        daily_data_list.append(box)

    # print(equities)
    # print(daily_data_list)
    print 'insert ', len(daily_data_list), 'daily_data_lists'
    asset_db_writer.write(equities=equities)
    day_bar_writer.write(data=daily_data_list)
    adjustment_writer.write()  # ensure the table exist, no data


print "begin!!!!"
register('anyquant-bundle', anyquant_ingest)
