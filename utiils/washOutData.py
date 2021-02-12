import os
import datetime
import pandas as pd

cols1 = ['Redmine', '不具合\n管理/temaheras', 'リリース\n承認', '障害\nランク', '概要', '区分', 'マージ方法', 'UT', 'IT', '回帰', 'UAT', 'ステ', '本番', '開発ブランチ\nリビジョン', 'マージ\nチェック', '検証ブランチリビジョン', 'マージチェック', 'Trunk\nリビジョン']
cols2 = ['Redmine', '不具合\n管理', 'リリース\n承認', '障害\nランク', '概要', '区分', 'マージ方法', 'UT', 'IT', '回帰', 'UAT', 'ステ', '本番', '開発ブランチ\nリビジョン', 'マージ\nチェック', '検証ブランチリビジョン', 'マージチェック', 'Trunk\nリビジョン']
basePath = r"\\Ds.inte.co.jp\IBS\IBSO\100_開発PJ\050_テンプ派遣基幹システム\98.TeamFolders\19.構成管理\01.GENESIS\01.リリース管理\リリース証跡"
src_files = []
tar_files = []


def find_files(base_path):
    if os.path.exists(base_path):
        for root, dirs, files in os.walk(basePath):
            for file1 in files:
                src_file = os.path.join(root, file1)
                if src_file.endswith('本C,Aリリース.xlsm') or src_file.endswith('帳票(本番C,A)リリース.xlsm'):
                    src_files.append(src_file)


def wash_out_files(files):
    for f in files:
        str(f)
        time1 = datetime.datetime(2020, 10, 1, 0, 0, 0, 0)
        time2 = datetime.datetime.fromtimestamp(os.stat(f).st_ctime)
        dayCount = (time1 - time2).days
        if dayCount < 0:
            tar_files.append(f)


def get_data(file_path):
    data = pd.DataFrame(pd.read_excel(file_path, sheet_name='概要&リリースノート', usecols=cols1, header=39, index_col=0))
    # data.dropna(how='all').to_csv('data01.cvs', mode='a', header=None)
    data.dropna(axis=0, subset=['開発ブランチ\nリビジョン']).to_csv('data01.cvs', mode='a')


def get_data2(file_path):
    data = pd.DataFrame(pd.read_excel(file_path, sheet_name='概要&リリースノート', usecols=cols2, header=9, index_col=0))
    data.dropna(axis=0, subset=['開発ブランチ\nリビジョン']).to_csv('data01.cvs', mode='a')


find_files(basePath)
if src_files is not None:
    wash_out_files(src_files)
    for file in tar_files:
        if file.endswith('本C,Aリリース.xlsm'):
            print(file.split('\\')[-1].split('_')[1])
            get_data(file)
        elif file.endswith('帳票(本番C,A)リリース.xlsm'):
            print(file.split('\\')[-1].split('_')[1])
            get_data2(file)
        else:
            ""