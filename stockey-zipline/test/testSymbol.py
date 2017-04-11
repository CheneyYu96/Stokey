# coding: UTF-8

# run with: zipline run -f testSymbol.py -s 2014-11-1 -e 2015-11-1 -b anyquant-bundle -o testSymbol.pickle


from zipline.api import (
    history,
    order,
    record,
    symbol,
)


def initialize(context):
    context.i = 0


def handle_data(context, data):

    sym = symbol('sh600606')

    order(sym, 100)

