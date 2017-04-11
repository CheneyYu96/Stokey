# coding: UTF-8

# run with: zipline run -f testHistory.py -s 2014-11-1 -e 2015-11-1 -b anyquant-bundle -o testHistory.pickle


from zipline.api import (
    order_target,
    record,
    symbol,
)


def initialize(context):
    context.i = 0


def handle_data(context, data):
    if context.i < 30:
        context.i += 1
        return

    sym = symbol('sh600606')
    short_mavg = data.history(sym, 'price', 6, '1d').mean()
    long_mavg = data.history(sym, 'price', 12, '1d').mean()
    if short_mavg > long_mavg:
        # order_target orders as many shares as needed to
        # achieve the desired number of shares.
        order_target(sym, 100)
    elif short_mavg < long_mavg:
        order_target(sym, 0)

    # Save values for later inspection
    record(LDKG=data[sym].price,
           short_mavg=short_mavg,
           long_mavg=long_mavg)
