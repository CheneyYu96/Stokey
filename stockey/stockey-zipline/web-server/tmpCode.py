# coding: UTF-8

# run with: zipline run -f random.py -s 2014-11-1 -e 2015-11-1 -o random.pickle --capital-base 10000001


from zipline.api import (
    history,
    order,
    record,
    symbol,
)


def initialize(context):
    context.i = 0


def handle_data(context, data):

    sym = symbol('AAPL')

    order(sym, 100)