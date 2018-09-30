#! /bin/sh -e
#set -x

apk update
apk add curl

wupiaowt() {
    # [w]ait [u]ntil [p]ort [i]s [a]ctually [o]pen [w]ith [t]imeout <who> <timeout> <target>
    if [ -z "$1" ] || [ -z "$2" ] || [ -z "$3" ]; then
        echo 'Invalid parameters'
        exit 1
    fi
    if ! timeout -t $2 -s KILL /bin/sh -c "until curl -XGET -o /dev/null -sf 'http://$3'; do sleep 1s && echo '$1 waiting for $3 ...'; done"; then
        echo "$0 timeout: $3 was unreachable"
        exit 137
    fi
}

wupiaowt "$0" 70 "grafana:3000"

#wupiaowt "$0" 70 "influxdb:8086"


STATUSCODE=$(curl -i -XPOST 'http://influxdb:8086/query?q=CREATE+DATABASE+%22telegraf%22&db=_internal' \
                  --write-out "%{http_code}" --output /dev/stderr)

if [ $STATUSCODE -ne 200 ]; then
    echo "Unable to create telegraf db in influxdb"
    exit 1
fi

STATUSCODE=$(curl -i -XPOST 'http://influxdb:8086/query?q=CREATE+DATABASE+%22perfana_trends%22&db=_internal' \
                  --write-out "%{http_code}" --output /dev/stderr)

if [ $STATUSCODE -ne 200 ]; then
    echo "Unable to create perfana_trends db in influxdb"
    exit 1
fi


STATUSCODE=$(curl -i -XPOST 'http://foobar:foobar@grafana:3000/api/datasources' \
                  --write-out "%{http_code}" --output /dev/stderr \
                  --header 'Content-Type: application/json' \
                  --data-binary "{\"name\":\"influxdb telegraf\",\"type\":\"influxdb\",\"access\":\"proxy\",\"url\":\"http://influxdb:8086\",\"password\":\"foobar\",\"user\":\"foobar\",\"database\":\"telegraf\",\"basicAuth\":false,\"isDefault\":true}")

if [ $STATUSCODE -ne 200 ]; then
    echo "Unable to initialise the influxdb telegraf data source"
    exit 1
fi

STATUSCODE=$(curl -i -XPOST 'http://foobar:foobar@grafana:3000/api/datasources' \
                  --write-out "%{http_code}" --output /dev/stderr \
                  --header 'Content-Type: application/json' \
                  --data-binary "{\"name\":\"influxdb perfana trends\",\"type\":\"influxdb\",\"access\":\"proxy\",\"url\":\"http://influxdb:8086\",\"password\":\"foobar\",\"user\":\"foobar\",\"database\":\"perfana_trends\",\"basicAuth\":false,\"isDefault\":true}")

if [ $STATUSCODE -ne 200 ]; then
    echo "Unable to initialise the influxdb perfana_trends data source"
    exit 1
fi

STATUSCODE=$(curl -i -XPOST 'http://foobar:foobar@grafana:3000/api/datasources' \
                  --write-out "%{http_code}" --output /dev/stderr \
                  --header 'Content-Type: application/json' \
                  --data-binary "{\"name\":\"influxdb gatling\",\"type\":\"influxdb\",\"access\":\"proxy\",\"url\":\"http://influxdb:8086\",\"password\":\"foobar\",\"user\":\"foobar\",\"database\":\"gatling\",\"basicAuth\":false,\"isDefault\":true}")

if [ $STATUSCODE -ne 200 ]; then
    echo "Unable to initialise the influxdb Gatling data source"
    exit 1
fi


STATUSCODE=$(curl -i -XPOST 'http://foobar:foobar@grafana:3000/api/dashboards/import' \
                  --write-out "%{http_code}" --output /dev/stderr \
                  --header 'Content-Type: application/json' \
                  --data-binary @/gatling.json)

if [ $STATUSCODE -ne 200 ]; then
    echo "Unable to create the gatling dashboard"
    exit 1
fi


STATUSCODE=$(curl -i -XPOST 'http://foobar:foobar@grafana:3000/api/dashboards/import' \
                  --write-out "%{http_code}" --output /dev/stderr \
                  --header 'Content-Type: application/json' \
                  --data-binary @/system.json)

if [ $STATUSCODE -ne 200 ]; then
    echo "Unable to create the system dashboard"
    exit 1
fi


STATUSCODE=$(curl -i -XPOST 'http://foobar:foobar@grafana:3000/api/dashboards/import' \
                  --write-out "%{http_code}" --output /dev/stderr \
                  --header 'Content-Type: application/json' \
                  --data-binary @/perfana_trends.json)

if [ $STATUSCODE -ne 200 ]; then
    echo "Unable to create the Perfana trends dashboard"
    exit 1
fi


while true; do
    exec tail -s 6000000 -f /dev/null
    sleep 1s
done
