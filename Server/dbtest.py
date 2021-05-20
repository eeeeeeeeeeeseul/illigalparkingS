from datetime import datetime
import sqlite3


def dbcon():
    return sqlite3.connect('parking.db')


# 차량정보
def create_carinfo():
    try:
        db = dbcon()
        c = db.cursor()
        c.execute("CREATE TABLE carinfo (carnum	TEXT, phonenum INTEGER, PRIMARY KEY(carnum))")
        db.commit()
    except Exception as e:
        print('db error:', e)
    finally:
        db.close()


# 회원정보
def create_userinfo():
    try:
        db = dbcon()
        c = db.cursor()
        c.execute(
            "CREATE TABLE userinfo (usernum TEXT, username TEXT, userid TEXT, userpw TEXT, location TEXT,FOREIGN KEY (location) REFERENCES cctvinfo(location), PRIMARY KEY(usernum))")
        db.commit()
    except Exception as e:
        print('db error:', e)
    finally:
        db.close()


# cctv정보
def create_cctvinfo():
    try:
        db = dbcon()
        c = db.cursor()
        c.execute("CREATE TABLE cctvinfo (cctvnum TEXT,location TEXT, cctvname TEXT, PRIMARY KEY(cctvnum))")
    except Exception as e:
        print('db error:', e)
    finally:
        db.close()


# 불법주정차 정보
def create_illigal_parking():
    try:
        db = dbcon()
        c = db.cursor()
        c.execute(
            "CREATE TABLE illigal_parking (illigal_num INTEGER, carnum TEXT, illigal_time TEXT, cctvname text, FOREIGN KEY (carnum) REFERENCES carinfo(carnum),FOREIGN KEY (cctvname) REFERENCES cctvinfo(cctvname), PRIMARY KEY (illigal_num) )")
        db.commit()
    except Exception as e:
        print('db error:', e)
    finally:
        db.close()


def insert_carinfo(carnum, phonenum):
    try:
        db = dbcon()
        c = db.cursor()
        setdata = (carnum, phonenum)
        c.execute("INSERT INTO carinfo VALUES (?,?)", setdata)
        db.commit()
    except Exception as e:
        print('db error:', e)
    finally:
        db.close()


def insert_userinfo(usernum, username, userid, userpw, location):
    try:
        db = dbcon()
        c = db.cursor()
        setdata = (usernum, username, userid, userpw, location)
        c.execute("INSERT INTO userinfo VALUES (?,?,?,?,?)", setdata)
        db.commit()
    except Exception as e:
        print('db error:', e)
    finally:
        db.close()


def insert_illigalinfo(illigalnum, carNum, illigaltime, cctvname):
    try:
        db = dbcon()
        c = db.cursor()
        setdata = (illigalnum, carNum, illigaltime, cctvname)
        c.execute("INSERT INTO illigal_parking VALUES (?,?,?,?)", setdata)
        db.commit()
    except Exception as e:
        print('db error:', e)
    finally:
        db.close()


def select_all_illigalinfo():
    ret = list()
    try:
        db = dbcon()
        c = db.cursor()
        c.execute("SELECT * FROM illigal_parking")
        ret = c.fetchall()
    except Exception as e:
        print('db error:', e)
    finally:
        db.close()
        return ret


def select_all_userinfo():
    ret = list()
    try:
        db = dbcon()
        c = db.cursor()
        c.execute("SELECT * FROM userinfo")
        ret = c.fetchall()
    except Exception as e:
        print('db error:', e)
    finally:
        db.close()
        return ret


def select_all_carinfo():
    ret = list()
    try:
        db = dbcon()
        c = db.cursor()
        c.execute("SELECT * FROM carinfo")
        ret = c.fetchall()
    except Exception as e:
        print('db error:', e)
    finally:
        db.close()
        return ret


def user_login(user_id, user_pw):
    ret = list()
    try:
        db = dbcon()
        db.row_factory = sqlite3.Row
        c = db.cursor()
        query_data = (user_id, user_pw)
        print(query_data)
        c.execute("SELECT usernum, username, location FROM userinfo WHERE userid = ? AND userpw = ?", query_data)
        ret = c.fetchall()
        print(ret)
    except Exception as e:
        print('db error:', e)
    finally:
        db.close()
        return ret


def select_all_illigalinfo():
    ret = list()
    try:
        db = dbcon()
        c = db.cursor()
        c.execute("SELECT * FROM illigal_parking")
        ret = c.fetchall()
    except Exception as e:
        print('db error:', e)
    finally:
        db.close()
        return ret


def select_today_illegal(get_date):
    ret = list()
    try:
        db = dbcon()
        db.row_factory = sqlite3.Row
        c = db.cursor()
        c.execute("SELECT * FROM illigal_parking")
        illegal_list = c.fetchall()

        for illegal in illegal_list:
            date_text = illegal['illigal_time']
            # 2021-04-26-17:40:25
            print(date_text[0:10])
            print(get_date)
            print(date_text[0:10] == get_date)
            if date_text[0:10] == get_date:
                ret.append(dict(illegal))

    except Exception as e:
        print('db error:', e)
    finally:
        db.close()
        return ret

def select_cctv(location):
    ret = list()
    try:
        db = dbcon()
        db.row_factory = sqlite3.Row
        c = db.cursor()
        # c.execute("SELECT * FROM cctvinfo WHERE location LIKE '% ? %'", [location])
        c.execute("SELECT * FROM cctvinfo")
        cctv_list = c.fetchall()

        for cctv in cctv_list:
            print(cctv)
            if location in cctv['location']:
                ret.append(dict(cctv))

    except Exception as e:
        print('db error:', e)
    finally:
        db.close()
        return ret


def select_user(location):
    ret = list()
    try:
        db = dbcon()
        db.row_factory = sqlite3.Row
        c = db.cursor()
        c.execute("SELECT usernum, username, location FROM userinfo")
        user_list = c.fetchall()
        print(user_list)
        for user in user_list:
            print(user)
            if location in user['location']:
                ret.append(dict(user))

    except Exception as e:
        print('db error:', e)
    finally:
        db.close()
        return ret

# dummyDB = dbcon()
# dummyCursor = dummyDB.cursor()
# dummyCursor.execute("INSERT INTO cctvinfo VALUES (1, '서울특별시 동작구 사당동', '사당역 1번출구')")
# dummyCursor.execute("INSERT INTO cctvinfo VALUES (2, '서울특별시 동작구 사당동', '사당역 2번출구')")
# dummyCursor.execute("INSERT INTO cctvinfo VALUES (3, '서울특별시 동작구 사당동', '사당역 3번출구')")
# dummyDB.commit()