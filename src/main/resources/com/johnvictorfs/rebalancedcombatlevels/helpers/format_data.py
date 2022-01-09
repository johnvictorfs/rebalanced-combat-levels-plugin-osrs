import json

new_data = []

with open('monsters-complete.json', 'r') as f:
    data = json.load(f)
    keys_to_keep = [
        'id',
        'name',
        'combat_level',
        'hitpoints',
        'attack_level',
        'strength_level',
        'defence_level',
        'magic_level',
        'ranged_level'
    ]

    for value in data.values():
        new_data.append({ k: value[k] for k in keys_to_keep })

with open('monsters-complete.json', 'w') as f:
    json.dump(new_data, f)
