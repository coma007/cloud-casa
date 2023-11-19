import React, { useEffect, useRef, useState } from "react";
import GoogleMapReact from 'google-map-react';
import { LocationService } from "./LocationService";

export interface Coordinates {
    lat: number,
    lng: number
}

const Map = ({ setAddress }) => {

    const apiKey: string | undefined = process.env.REACT_APP_GOOGLE_MAPS_API_KEY;

    const center: Coordinates = {
        lat: 45.2453226422613,
        lng: 19.847816366597577
    }

    const [marker, setMarker] = useState<Coordinates>();
    const [mapMarker, setMapMarker] = useState<google.maps.Marker | undefined>();

    const [map, setMap] = useState();
    const [maps, setMaps] = useState();


    const handleClick = ({ x, y, lat, lng, event }: any) => {
        setMarker({
            lat: lat,
            lng: lng,
        });
        LocationService.getLocation(lat, lng).then((value) => setAddress(value))
    };

    useEffect(() => {
        renderMarkers(map, maps);
    }, [marker])

    const renderMarkers = (map, maps) => {
        if (mapMarker != undefined) {
            mapMarker.setMap(null);
        }
        if (marker !== undefined) {
            let mapMarker = new maps.Marker({
                position: { lat: marker.lat, lng: marker.lng },
                map,
                title: 'Marker'
            });
            setMapMarker(mapMarker);
            return mapMarker;
        }
        setMap(map);
        setMaps(maps);
    };

    return (
        <div style={{ height: '70vh', width: '100%' }}>
            <GoogleMapReact
                bootstrapURLKeys={{ key: apiKey }}
                defaultCenter={{ lat: center.lat, lng: center.lng }}
                defaultZoom={16}
                yesIWantToUseGoogleMapApiInternals
                onClick={handleClick}
                onGoogleApiLoaded={({ map, maps }) => { renderMarkers(map, maps) }}
            >
            </GoogleMapReact>
        </div>
    );
};

export default Map;