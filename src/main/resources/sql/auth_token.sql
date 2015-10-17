SELECT *
FROM auth_tokens
WHERE id = (
  SELECT auth_token_id
  FROM provider_users
  WHERE user_id = ?
        AND provider_id = (
    SELECT id
    FROM providers
    WHERE name = ?
    LIMIT 1
  )
  LIMIT 1
)
LIMIT 1
