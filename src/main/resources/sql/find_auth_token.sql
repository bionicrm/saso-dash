SELECT *
FROM auth_tokens
WHERE id = (
  SELECT auth_token_id
  FROM service_users
  WHERE user_id = ?
        AND service_id = (
    SELECT id
    FROM services
    WHERE name = ?
    LIMIT 1
  )
  LIMIT 1
)
LIMIT 1
