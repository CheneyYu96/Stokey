from pandas import Series

if __name__ == '__main__':
    days = 5
    serie = Series([1, 2, 3, 4, 5], index=['a', 'b', 'c', 'd', 'e'])
    boolVar = True
    for i in range(days-1):
        if serie[-i-2] < serie[-i-1]:
            print(str(serie[-i-2]), "less than", str(serie[-i-1]))
            boolVar = False
            break

    print(boolVar)
