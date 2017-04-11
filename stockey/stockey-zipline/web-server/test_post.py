import urllib2
import json


def send(url, data):
    request = urllib2.Request(url=url, data=data)
    request.add_header('Content-Type', 'application/json')
    print urllib2.urlopen(request).read()


if __name__ == '__main__':
    # prepare python code
    python_code = ''
    python_file = open('compiled2.py')
    line = python_file.readline()
    while line:
        python_code += line
        line = python_file.readline()

    dic = {'begin_date': '2013-1-1', 'end_date': '2014-1-1', 'capital': '8888888', 'code': python_code}
    send('http://104.236.174.190:5000/', json.dumps(dic))
