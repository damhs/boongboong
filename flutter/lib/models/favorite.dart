// lib/models/favorite.dart
class Favorite {
  final String id;
  final String departure;
  final String arrival;

  Favorite({required this.id, required this.departure, required this.arrival});

  factory Favorite.fromJson(Map<String, dynamic> json) {
    return Favorite(
      id: json['id'],
      departure: json['departure'],
      arrival: json['arrival'],
    );
  }
}