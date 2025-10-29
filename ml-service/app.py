from flask import Flask, request, jsonify
import pandas as pd
import mysql.connector
from sklearn.neighbors import NearestNeighbors
import numpy as np

app = Flask(__name__)

# MySQL connection - use same credentials as docker-compose.yml
db_config = {
    'host': 'mysql-container',  # or service name from docker-compose
    'user': 'root',
    'password': 'yourpassword',
    'database': 'car_rental_db'
}

def fetch_car_data():
    conn = mysql.connector.connect(**db_config)
    query = "SELECT id, brand, model, price_per_day, seats, fuel_type FROM cars"
    df = pd.read_sql(query, conn)
    conn.close()
    return df

@app.route('/train', methods=['POST'])
def train_model():
    df = fetch_car_data()
    if df.empty:
        return jsonify({"error": "No car data found"}), 400

    global model, car_df
    car_df = df
    features = pd.get_dummies(df[['price_per_day', 'seats', 'fuel_type']], drop_first=True)
    model = NearestNeighbors(n_neighbors=3, algorithm='auto').fit(features)
    return jsonify({"message": "Model trained successfully", "total_cars": len(df)})

@app.route('/recommend', methods=['POST'])
def recommend():
    req = request.get_json()
    if 'price_per_day' not in req or 'seats' not in req or 'fuel_type' not in req:
        return jsonify({"error": "Missing required fields"}), 400

    input_data = pd.DataFrame([req])
    input_features = pd.get_dummies(input_data[['price_per_day', 'seats', 'fuel_type']], drop_first=True)

    # Align with trained model columns
    input_features = input_features.reindex(columns=pd.get_dummies(car_df[['price_per_day', 'seats', 'fuel_type']], drop_first=True).columns, fill_value=0)

    distances, indices = model.kneighbors(input_features)
    recommendations = car_df.iloc[indices[0]].to_dict(orient='records')

    return jsonify({"recommendations": recommendations})

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5001)
