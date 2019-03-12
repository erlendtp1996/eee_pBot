from os import environ
from flask import Flask

app = Flask(__name__)
app.run(environ.get('PORT'))

@app.route('/')
def hello_world():
    return 'Hello, World!'

if __name__ == '__main__':
    port = int(environ.get("PORT", 5000))
    app.run(host='0.0.0.0', port=port)
