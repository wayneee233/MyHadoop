import pandas as pd
import openpyxl


pd.set_option('display.unicode.ambiguous_as_wide', True)
pd.set_option('display.unicode.east_asian_width', True)
pd.set_option('display.max_columns', 1000)
pd.set_option('display.width', 1000)
pd.set_option('display.max_colwidth', 1000)


def read_excel(filepath):
    data = pd.DataFrame(pd.read_csv(filepath))
    data.to_excel(r"C:\Users\jiang.weiyu\Desktop\test.xlsx", sheet_name="Sheet4", header=False)


read_excel('data01.cvs')