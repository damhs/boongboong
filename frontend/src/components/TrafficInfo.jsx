import React from 'react';

function TrafficInfo({ data, loading }) {
  if (loading) return <p>Loading traffic data...</p>;
  if (!data) return <p>No traffic data available.</p>;

  return (
    <div style={styles.container}>
      <h2 style={styles.heading}>Traffic Information</h2>
      <ul style={styles.list}>
        {data.map((item, index) => (
          <li key={index} style={styles.listItem}>
            {item.location}: {item.status}
          </li>
        ))}
      </ul>
    </div>
  );
}

const styles = {
  container: {
    padding: '10px',
    border: '1px solid #ddd',
    borderRadius: '5px',
    marginTop: '20px',
  },
  heading: {
    fontSize: '1.2rem',
    marginBottom: '10px',
  },
  list: {
    listStyleType: 'none',
    padding: 0,
  },
  listItem: {
    marginBottom: '5px',
  },
};

export default TrafficInfo;
