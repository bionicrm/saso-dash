SELECT *
FROM provider_users
WHERE user_id = ?
      AND provider_id = (
  SELECT id
  FROM providers
  WHERE name = ?
)
LIMIT 1
