import pandas as pd
import sys

if len(sys.argv) < 3:
    print("error: file not found")
    sys.exit(1)

path = sys.argv[1]  # the dir containing xx.pickle, the performance, in DataFrame format
filename = sys.argv[2]  # name of the xx.pickle file
perf = pd.read_pickle(path + "/" + filename + ".pickle")

del perf['period_close']
del perf['period_open']

rj = perf.to_json(orient='index', date_format='iso')

json_file = open(path + "/" + filename + ".json", 'w')
json_file.write(rj)
json_file.close()
