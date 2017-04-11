from zipline.api import symbol,order
def initialize(context):
    return

def handle_data(context, data):
    con0_short_avg = data.history(symbol('sh601398'), 'close', 12, '1d').mean()
    con0_long_avg = data.history(symbol('sh601398'), 'close', 26, '1d').mean()


    con1_days_data = data.history(symbol('sh601398'), 'close', 3, '1d')
    con1_boolVar = True
    for i in range(3-1):
        if con1_days_data[-i-2] > con1_days_data[-i-1]:
            con1_boolVar = False
            break

    if (con0_short_avg < con0_long_avg and con1_boolVar):

        order(symbol('sh601398'), 199)