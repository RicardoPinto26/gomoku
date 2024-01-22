set -e

host="$1"
shift
cmd="$@"

echo >&2 "waiting for postgres on $host"
until pg_isready -h "$host"; do
  echo >$2 "Postgres is unavailable - sleeping"
  sleep 1
done

echo >&2 "Postgres is up - executing command '$cmd'"
exec "$cmd"
