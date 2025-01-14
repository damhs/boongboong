// lib/models/place.dart
class PlaceModel {
  final String id;
  final String name;
  final double latitude;
  final double longitude;

  PlaceModel({
    required this.id,
    required this.name,
    required this.latitude,
    required this.longitude,
  });

  factory PlaceModel.fromJson(Map<String, dynamic> json) {
    return PlaceModel(
      id: json['id'],
      name: json['name'],
      latitude: json['latitude'].toDouble(),
      longitude: json['longitude'].toDouble(),
    );
  }

  Map<String, dynamic> toJson() => {
        'id': id,
        'name': name,
        'latitude': latitude,
        'longitude': longitude,
      };
}

// Wrapper to include place type
class PlaceWithType {
  final PlaceModel place;
  final String placeType; // 'departure' or 'arrival'

  PlaceWithType({required this.place, required this.placeType});
}
