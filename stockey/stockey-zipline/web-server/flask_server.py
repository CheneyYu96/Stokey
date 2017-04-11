from flask import Flask
from flask import request
import pandas as pd
import subprocess
import zipline
from zipline import run_algorithm

app = Flask(__name__)

# TODO 动态添加方法，提高性能


@app.route('/', methods=['POST'])
def home():
    # prepare code file
    python_code = request.json.get('code')
    tmp_code_file = open('tmpCode.py', 'w')
    tmp_code_file.writelines(python_code)
    tmp_code_file.close()
    print("===========python code==========")
    print(python_code)
    print("===========python code end==========")

    # format the command
    command = "zipline run -f {0} -s {1} -e {2} -o {3} --capital-base {4} --bundle anyquant-bundle" \
        .format('tmpCode.py', request.json.get('begin_date'), request.json.get('end_date'), 'tmp2.pickle', request.json.get('capital'))

    print command
    # Warning: shell=True only on Linux machine
    status = subprocess.call(command, shell=True)
    print 'status:', status

    perf = pd.read_pickle('tmp2.pickle')
    del perf['period_close']
    del perf['period_open']

    reduced_perf = perf
    rj = reduced_perf.to_json(orient='index', date_format='iso')

    return rj


if __name__ == '__main__':
    app.run(host='0.0.0.0')
