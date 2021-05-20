import json

from flask import Flask, render_template, request, redirect, url_for, abort, jsonify, Response, make_response
import dbtest

app = Flask(__name__)
app.config['JSON_AS_ASCII'] = False

dbtest.create_userinfo()
dbtest.create_carinfo()
dbtest.create_illigal_parking()
dbtest.create_cctvinfo()


@app.route('/')
def front():
    return 'hello'


# 회원정보 입력
@app.route('/userinfo')
def userinfo():
    return render_template("userinfo.html")


# 차량정보 입력
@app.route('/carinfo')
def carinfo():
    return render_template("carinfo.html")


# 불법주정차 정보 입력
@app.route('/illigalinfo')
def illigalinfo():
    return render_template("illigalinfo.html")


# 회원정보 DB저장
@app.route('/usermethod', methods=['GET', 'POST'])
def usermethod():
    data = request.form
    dbtest.insert_userinfo(data['usernum'], data['username'], data['userid'], data['userpw'], data['location'])
    return jsonify(result="success", result2=data)


# 차량정보 DB저장
@app.route('/carmethod', methods=['POST'])
def carmethod():
    data = request.get_json()
    dbtest.insert_carinfo(data['carnum'], data['phonenum'])
    return jsonify(result="success", result2=data)


# 불법 주정차 정보 DB 저장
@app.route('/illigalmethod', methods=['POST'])
def illigalmethod():
    data = request.get_json()
    print(data)
    dbtest.insert_illigalinfo(data['illigalnum'], data['carnum'], data['illigaltime'], data['cctvname'])
    return jsonify(result="success", result2=data)


# 회원정보 조회
@app.route('/getuserinfo')
def getuserinfo():
    info = dbtest.select_all_userinfo()
    return render_template("user.html", data=info)


# 차량정보 조회
@app.route('/getcarinfo')
def getcarinfo():
    info = dbtest.select_all_carinfo()
    return render_template("car.html", data=info)


# 불법주정차 정보 조회
@app.route('/getilligalinfo')
def getilligalinfo():
    info = dbtest.select_all_illigalinfo()
    return render_template("illigal.html", data=info)


# 로그인
@app.route('/userlogin', methods=['POST'])
def user_login():
    data = request.form
    print(data)
    info = dbtest.user_login(data["id"], data["pw"])
    if len(info) > 0:
        return jsonify(result=True, data=dict(info[0]))
    else:
        return jsonify(result=False)


# 로그인
@app.route('/get_illegal', methods=['GET'])
def get_illegal_info():
    select_date = request.args["date"]
    illegal_list = dbtest.select_today_illegal(select_date)
    print(illegal_list)
    return jsonify(result=True, data=illegal_list)


# 로그인
@app.route('/search_cctv', methods=['GET'])
def get_cctv_list():
    location = request.args["location"]
    cctv_list = dbtest.select_cctv(location)
    print(cctv_list)
    return jsonify(result=True, data=cctv_list)


# 로그인
@app.route('/search_user', methods=['GET'])
def get_user_list():
    print(request.args)
    location = request.args["location"]
    user_list = dbtest.select_user(location)
    print(user_list)
    return jsonify(result=True, data=user_list)


app.run(host='0.0.0.0', port=8080)
