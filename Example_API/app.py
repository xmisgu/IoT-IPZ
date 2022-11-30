from flask import Flask, request, jsonify, current_app
from flask_sqlalchemy import SQLAlchemy 
from flask_marshmallow import Marshmallow 
import os

# Init app
app = Flask(__name__)
basedir = os.path.abspath(os.path.dirname(__file__))
# Database
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///' + os.path.join(basedir, 'db.sqlite')
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
# Init db
db = SQLAlchemy(app)
# Init ma
ma = Marshmallow(app)

# Product Class/Model
class Product(db.Model):
  id = db.Column(db.Integer, primary_key=True, unique=True)
  temp = db.Column(db.Float)
  humidity = db.Column(db.Float)
  pressure = db.Column(db.Float)

  def __init__(self, temp, humidity, pressure):
    self.temp = temp
    self.humidity = humidity
    self.pressure = pressure

# Product Schema
class ProductSchema(ma.Schema):
  class Meta:
    fields = ('id', 'temp', 'humidity', 'pressure')

# Init schema
product_schema = ProductSchema()
products_schema = ProductSchema(many=True)

# Create a Product
@app.route('/send', methods=['POST'])
def add_product():
  temp = request.json['temp']
  humidity = request.json['humidity']
  pressure = request.json['pressure']
  new_product = Product(temp, humidity, pressure)

  db.session.add(new_product)
  db.session.commit()

  return product_schema.jsonify(new_product)

# Get All Products
@app.route('/see_all', methods=['GET'])
def get_products():
  all_products = Product.query.all()
  result = products_schema.dump(all_products)
  return jsonify(result)

# Get Single Products
@app.route('/see/<id>', methods=['GET'])
def get_product(id):
  product = Product.query.get(id)
  return product_schema.jsonify(product)

#Get Last Product
@app.route('/see/last', methods=['GET'])
def get_last_product():
  last_product = db.session.query(Product).order_by(Product.id.desc()).first()
  return product_schema.jsonify(last_product)

# Create a Product
@app.route('/send/<id>', methods=['PUT'])
def update_product(id):
  product = Product.query.get(id)

  temp = request.json['temp']
  humidity = request.json['humidity']
  pressure = request.json['pressure']

  product.temp = temp
  product.pressure = pressure
  product.humidity = humidity

  db.session.commit()

  return product_schema.jsonify(product)

#Delete Product
@app.route('/delete/<id>', methods=['DELETE'])
def delete_product(id):
  product = Product.query.get(id)
  db.session.delete(product)
  db.session.commit()
  return product_schema.jsonify(product)




#Run server
if __name__ == '__main__':
    with app.app_context():
        pass

    app.run(host= '192.168.0.207')
    