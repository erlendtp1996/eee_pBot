import base64
import requests
import datetime
import json

def getForDate():
    return datetime.datetime.today().strftime('%Y%m%d')

def send_request():
    apikey_token = "c1bb6ebc-a473-4626-832b-de4db6"
    password = "eeepBot1"
    pull_url = "https://api.mysportsfeeds.com/v1.2/pull/nba/2018-2019-regular/daily_game_schedule.json?fordate=" + getForDate()
    try:
        print ( 'About to fetch daily games for ' + str( getForDate() ))
        response = requests.get(
            url = pull_url,
            params = { "fordate": "20161121" },
            headers = { "Authorization": "Basic " + base64.b64encode('{}:{}'.format(apikey_token, password).encode('utf-8')).decode('ascii') })
        return json.loads(response.content)
    except requests.exceptions.RequestException:
        print ( 'ERROR Fetching Daily Games' )
        return False

def getDailyGameTweets():
    dailyGames = send_request()
    if dailyGames != False:
        dailyGameList = []
        for game in dailyGames["dailygameschedule"]["gameentry"]:
            dailyGameList.append(str(game["homeTeam"]["Abbreviation"] + " vs " + game["awayTeam"]["Abbreviation"] + ": " + game["time"]))
        return dailyGameList
    else:
        return False;
