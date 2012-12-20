CREATE TABLE geographic_zone(
  id INTEGER PRIMARY KEY,
  name VARCHAR(30) NOT NULL,
  level INTEGER NOT NULL REFERENCES geopolitical_levels(id),
  parent INTEGER REFERENCES geographic_zone(id)
);