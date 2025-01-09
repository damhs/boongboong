import React, { useEffect, useRef } from 'react';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';

function MapView() {
  const mapRef = useRef(null);

  useEffect(() => {
    if (!mapRef.current) {
      mapRef.current = L.map('map').setView([37.5665, 126.9780], 13); // 서울시 중심 좌표
      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; OpenStreetMap contributors',
      }).addTo(mapRef.current);
    }
  }, []);

  return <div id="map" style={styles.map}></div>;
}

const styles = {
  map: {
    width: '100%',
    height: '500px',
    marginTop: '20px',
  },
};

export default MapView;
