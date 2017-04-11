from zipline.api import symbol,order
def initialize(context):
    return

def handle_data(context, data):
    con0_short_avg = data.history(symbol('sh601398'), 'close', 12, '1d').mean()
    con0_long_avg = data.history(symbol('sh601398'), 'close', 26, '1d').mean()

    if con0_short_avg < con0_long_avg:

        order(symbol('sh601398'), 199)