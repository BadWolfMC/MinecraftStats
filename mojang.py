import base64
import json
import requests

profile_api_url = 'https://sessionserver.mojang.com/session/minecraft/profile/'

def compact_uuid(uuid):
    return uuid.replace('-', '')

# get player profile via Mojang's API
# the returned object is the first property ("textures") decoded
# may raise an error on failure
def get_player_profile(uuid):
    profile = requests.get(profile_api_url + compact_uuid(uuid)).json()
    if 'error' in profile:
        raise Exception(profile['errorMessage'])

    # FIXME: this is some heavy hardcoding right here, but what the API returns
    # does not seem to follow any reasonnable logic
    return json.loads(
        base64.b64decode(profile['properties'][0]['value']).decode())
