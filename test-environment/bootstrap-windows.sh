#! /bin/bash
#set -x


curl -i -XPOST 'http://localhost:8086/query?q=CREATE+DATABASE+%22telegraf%22&db=_internal'

curl -i -XPOST 'http://localhost:8086/query?q=CREATE+DATABASE+%22perfana_trends%22&db=_internal'

curl -i -XPOST 'http://foobar:foobar@localhost:3000/api/datasources' \
                  --header 'Content-Type: application/json' \
                  --data-binary "{\"name\":\"influxdb telegraf\",\"type\":\"influxdb\",\"access\":\"proxy\",\"url\":\"http://influxdb:8086\",\"password\":\"foobar\",\"user\":\"foobar\",\"database\":\"telegraf\",\"basicAuth\":false,\"isDefault\":true}"


curl -i -XPOST 'http://foobar:foobar@localhost:3000/api/datasources' \
                  --header 'Content-Type: application/json' \
                  --data-binary "{\"name\":\"influxdb perfana trends\",\"type\":\"influxdb\",\"access\":\"proxy\",\"url\":\"http://influxdb:8086\",\"password\":\"foobar\",\"user\":\"foobar\",\"database\":\"perfana_trends\",\"basicAuth\":false,\"isDefault\":true}"


curl -i -XPOST 'http://foobar:foobar@localhost:3000/api/datasources' \
                  --header 'Content-Type: application/json' \
                  --data-binary "{\"name\":\"influxdb gatling\",\"type\":\"influxdb\",\"access\":\"proxy\",\"url\":\"http://influxdb:8086\",\"password\":\"foobar\",\"user\":\"foobar\",\"database\":\"gatling\",\"basicAuth\":false,\"isDefault\":true}"



curl -i -XPOST 'http://foobar:foobar@localhost:3000/api/dashboards/import' \
                  --header 'Content-Type: application/json' \
                  --data-binary @./gatling.json


curl -i -XPOST 'http://foobar:foobar@localhost:3000/api/dashboards/import' \
                  --header 'Content-Type: application/json' \
                  --data-binary @./system.json

curl -i -XPOST 'http://foobar:foobar@localhost:3000/api/dashboards/import' \
                  --header 'Content-Type: application/json' \
                  --data-binary @./perfana_trends.json


