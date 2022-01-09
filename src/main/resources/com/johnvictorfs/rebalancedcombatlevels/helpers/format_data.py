import json
import urllib.request

new_data = []

url = 'https://www.osrsbox.com/osrsbox-db/monsters-complete.json'

r = urllib.request.urlopen(url)

data = json.loads(r.read().decode(r.info().get_param('charset') or 'utf-8'))

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
