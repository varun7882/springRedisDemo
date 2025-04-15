-- returns a list as response
local user_key = KEYS[1]
local number_key = KEYS[2]
local guesses_key = KEYS[3]
local guess = tonumber(ARGV[1])

redis.log(redis.LOG_NOTICE, 'Guess received: ' .. tostring(guess))
redis.log(redis.LOG_NOTICE, 'user received: ' .. tostring(user_key))
redis.log(redis.LOG_NOTICE, 'number received: ' .. tostring(number_key))
redis.log(redis.LOG_NOTICE, 'guesses_key received: ' .. tostring(guesses_key))
-- Get user data
local user_json = redis.call('GET', user_key)
if not user_json then
    return redis.error_reply('User not found')
end
redis.log(redis.LOG_NOTICE, 'fetching user ' .. tostring(user_json))
local user = cjson.decode(user_json)
redis.log(redis.LOG_NOTICE, 'fetching user as obj ' .. tostring(user))
local max_guesses = tonumber(user.maxGuessAllowed)

-- Get random number
local number = tonumber(redis.call('GET', number_key) or '0')
if number == 0 then
    return redis.error_reply('Game not started')
end

if guesses_made >= max_guesses then
    return redis.error_reply('Max guess made')
end

-- Add guess
redis.call('LPUSH', guesses_key, guess)
local guesses = redis.call('LRANGE', guesses_key, 0, -1)
local guesses_made = #guesses

redis.log(redis.LOG_NOTICE, 'processing ' .. tostring(max_guesses))

-- Check guess
local is_correct = (number == guess)

redis.log(redis.LOG_NOTICE, 'calculated result: ' .. tostring(is_correct))

-- Return result as list
return {guesses_made, cjson.encode(user.id), cjson.encode(is_correct), guesses}