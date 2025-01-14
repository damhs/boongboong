// lib/services/api_service.dart
import 'dart:convert';
import 'package:http/http.dart' as http;
import '../models/favorite.dart';
import '../models/path_model.dart';
import '../models/recent_history_item.dart';

class ApiService {
  final String baseUrl = "https://your-backend-url.com"; // Replace with your backend URL

  // Get Favorites
  Future<List<Favorite>> getFavorites(String userID) async {
    final response = await http.get(Uri.parse('$baseUrl/favorites/$userID'));

    if (response.statusCode == 200) {
      List<dynamic> data = jsonDecode(response.body);
      return data.map((json) => Favorite.fromJson(json)).toList();
    } else {
      throw Exception('Failed to load favorites');
    }
  }

  // Get Recent Paths
  Future<List<PathModel>> getRecentPaths(String userID) async {
    final response = await http.get(Uri.parse('$baseUrl/paths/$userID/recents'));

    if (response.statusCode == 200) {
      List<dynamic> data = jsonDecode(response.body);
      return data.map((json) => PathModel.fromJson(json)).toList();
    } else {
      throw Exception('Failed to load recent paths');
    }
  }

  // Get Recent History
  Future<List<RecentHistoryItem>> getRecentHistory(String userID) async {
    final response = await http.get(Uri.parse('$baseUrl/recents/$userID'));

    if (response.statusCode == 200) {
      List<dynamic> data = jsonDecode(response.body);
      return data.map((json) => RecentHistoryItem.fromJson(json)).toList();
    } else {
      throw Exception('Failed to load recent history');
    }
  }

  // Delete Path
  Future<void> deletePath(String pathId) async {
    final response = await http.delete(Uri.parse('$baseUrl/paths/$pathId'));

    if (response.statusCode != 200) {
      throw Exception('Failed to delete path');
    }
  }

  // Get Path Details (PathModel 포함 Segments)
  Future<PathModel> getPathDetails(String pathID) async {
    final response = await http.get(Uri.parse('$baseUrl/paths/$pathID'));

    if (response.statusCode == 200) {
      return PathModel.fromJson(jsonDecode(response.body));
    } else {
      throw Exception('Failed to load path details');
    }
  }

  // Delete Recent History Item
  Future<void> deleteRecentHistory(String itemId) async {
    final response = await http.delete(Uri.parse('$baseUrl/recents/$itemId'));

    if (response.statusCode != 200) {
      throw Exception('Failed to delete recent history item');
    }
  }

  // Get Place Details
  Future<Place> getPlaceDetails(String placeID) async {
    final response = await http.get(Uri.parse('$baseUrl/places/$placeID'));

    if (response.statusCode == 200) {
      return Place.fromJson(jsonDecode(response.body));
    } else {
      throw Exception('Failed to load place details');
    }
  }

  // Add or Update Path
  Future<void> addOrUpdatePath(String userID, String originID, String destinationID) async {
    // First, check if path exists
    final recentPaths = await getRecentPaths(userID);
    final existingPath = recentPaths.firstWhere(
        (path) => path.originID == originID && path.destinationID == destinationID,
        orElse: () => PathModel(id: '', pathID: ''));

    if (existingPath.pathID.isNotEmpty) {
      // Update existing path's createdAt
      final response = await http.put(
        Uri.parse('$baseUrl/paths/${existingPath.pathID}'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode({'createdAt': DateTime.now().toIso8601String()}),
      );

      if (response.statusCode != 200) {
        throw Exception('Failed to update existing path');
      }
    } else {
      // Add new path
      final response = await http.post(
        Uri.parse('$baseUrl/paths'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode({
          'userID': userID,
          'originID': originID,
          'destinationID': destinationID,
        }),
      );

      if (response.statusCode != 201) {
        throw Exception('Failed to add new path');
      }
    }
  }

  // Get Coordinates
  Future<Coordinates> getCoordinates(String placeID) async {
    final response = await http.get(Uri.parse('$baseUrl/places/$placeID'));

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      return Coordinates(
        latitude: data['latitude'],
        longitude: data['longitude'],
      );
    } else {
      throw Exception('Failed to load coordinates');
    }
  }

  // Search Path
  Future<PathModel> searchPath(Coordinates start, Coordinates goal) async {
    final response = await http.get(
      Uri.parse('$baseUrl/api/search-path').replace(queryParameters: {
        'start': '${start.longitude},${start.latitude}',
        'goal': '${goal.longitude},${goal.latitude}',
      }),
    );

    if (response.statusCode == 200) {
      // 서버가 PathModel JSON을 반환한다고 가정
      return PathModel.fromJson(jsonDecode(response.body));
    } else {
      throw Exception('Failed to search path');
    }
  }

  
}

// Models for Coordinates
class Coordinates {
  final double latitude;
  final double longitude;

  Coordinates({required this.latitude, required this.longitude});
}
