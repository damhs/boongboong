import React from 'react';

function About() {
  return (
    <div style={styles.container}>
      <h1 style={styles.heading}>About Our App</h1>
      <p style={styles.paragraph}>
        Our web application is designed to help drivers move seamlessly without being delayed by traffic signals. By leveraging real-time traffic data and advanced route optimization algorithms, we aim to:
      </p>
      <ul style={styles.list}>
        <li>Provide the most efficient routes for drivers.</li>
        <li>Reduce travel time and fuel consumption.</li>
        <li>Contribute to easing urban traffic congestion.</li>
      </ul>
      <p style={styles.paragraph}>
        Whether you're commuting, making deliveries, or just exploring the city, our app ensures a smoother driving experience. Join us in making traffic a little less stressful!
      </p>
    </div>
  );
}

const styles = {
  container: {
    padding: '20px',
    fontFamily: 'Arial, sans-serif',
    lineHeight: '1.6',
  },
  heading: {
    fontSize: '2rem',
    marginBottom: '10px',
  },
  paragraph: {
    fontSize: '1rem',
    marginBottom: '15px',
  },
  list: {
    listStyleType: 'disc',
    marginLeft: '20px',
    marginBottom: '15px',
  },
};

export default About;
