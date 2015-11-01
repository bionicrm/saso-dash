SELECT *
FROM service_users
WHERE user_id = ?
      AND service_id = (
  SELECT id
  FROM services
  WHERE name = ?
)
LIMIT 1
