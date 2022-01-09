import json

new_data = []
with open('monsters-complete.json', 'r') as f:
    data = json.load(f)

    for value in data.values():
        new_data.append(value)

with open('monsters-complete.json', 'w') as f:
    json.dump(new_data, f)
