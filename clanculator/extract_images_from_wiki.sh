#!/bin/sh

res="tmp"
wiki="http://clashofclans.wikia.com/wiki"

buildingGroups="Defensive_Buildings Army_Buildings Resource_Buildings"
#groups="Resource_Buildings Defensive_Buildings Army_Buildings Other_Buildings"

#troupGroups="Tier_1"
#groups="Resource_Buildings Defensive_Buildings Army_Buildings Other_Buildings"

function getImage() {
    url=$(echo $1 | sed "s/[0-9]*px/$2px/")
    file="${res}/drawable-$3/$4"
    curl -s "${url}"  > ${file}
}

function getImages() {
    images=$(curl -s $1 | grep $2 | sed 's/.*\(http[^ ]*png\).*/\1/')
    for image in ${images}; do
        echo $image
        name=$(echo ${image} | sed 's/.*[0-9]*px-\(.*\)/\1/' | tr '[:upper:]' '[:lower:]')
        echo "   ${name}..."
        getImage ${image} "48" "mdpi" ${name}
        getImage ${image} "72" "hdpi" ${name}
        getImage ${image} "96" "xhdpi" ${name}
        getImage ${image} "144" "xxhdpi" ${name}
    done
}

function getGroup() {
    items=$(curl -s $1 | grep '<td class="hovernav">' | sed 's;.*href="/wiki/\([^"]*\)".*;\1;')
    for item in ${items}; do
        if [ ${item} != "Traps" ]; then
            itemPage="${wiki}/${item}"
            echo "${itemPage}"
            getImages ${itemPage} $2
        fi
    done
}

for group in ${buildingGroups}; do
    echo ${group}
    getGroup "${wiki}/${group}" '<td><img.*clashofclans/images/thumb'
done

for group in ${troupGroups}; do
    echo ${group}
    getGroup "${wiki}/${group}" '<td><div style.*clashofclans/images/thumb'
done
