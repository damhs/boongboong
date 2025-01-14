// lib/pages/search_page.dart
import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../components/header.dart';
import '../components/search_bar.dart';
import '../components/arrival_time.dart';
import '../components/favorites.dart';
import '../components/recent_history.dart';
import '../models/favorite.dart';
import '../models/path_model.dart';
import '../models/recent_history_item.dart';
import '../services/api_service.dart';
import 'search_place_page.dart';

class SearchPage extends StatefulWidget {
  @override
  _SearchPageState createState() => _SearchPageState();
}

class _SearchPageState extends State<SearchPage> {
  final String userID = "1efd1a4b-706a-6e71-a44d-e7b1f23b2697";
  final ApiService apiService = ApiService();

  PlaceModel? departure;
  PlaceModel? arrival;
  List<Favorite> favorites = [];
  List<PathModel> recentPaths = [];
  List<RecentHistoryItem> recentHistory = [];
  PathModel? currentPath;

  bool isLoading = false;

  @override
  void initState() {
    super.initState();
    loadInitialData();
    fetchFavorites();
    fetchRecentPaths();
    fetchRecentHistory();
  }

  Future<void> loadInitialData() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    String? storedDeparture = prefs.getString('departure');
    String? storedArrival = prefs.getString('arrival');

    setState(() {
      departure = storedDeparture != null
          ? PlaceModel.fromJson(jsonDecode(storedDeparture))
          : null;
      arrival = storedArrival != null
          ? PlaceModel.fromJson(jsonDecode(storedArrival))
          : null;
    });
  }

  Future<void> fetchFavorites() async {
    try {
      List<Favorite> fetchedFavorites =
          await apiService.getFavorites(userID);
      setState(() {
        favorites = fetchedFavorites;
      });
    } catch (e) {
      print("Error fetching favorites: $e");
    }
  }

  Future<void> fetchRecentPaths() async {
    try {
      List<PathModel> fetchedPaths =
          await apiService.getRecentPaths(userID);
      setState(() {
        recentPaths = fetchedPaths;
      });
    } catch (e) {
      print("Error fetching recent paths: $e");
    }
  }

  Future<void> fetchRecentHistory() async {
    try {
      List<RecentHistoryItem> fetchedHistory =
          await apiService.getRecentHistory(userID);
      setState(() {
        recentHistory = fetchedHistory;
      });
    } catch (e) {
      print("Error fetching recent history: $e");
    }
  }

  Future<void> handlePathDelete(String pathId) async {
    try {
      await apiService.deletePath(pathId);
      setState(() {
        recentPaths.removeWhere((path) => path.pathID == pathId);
      });
    } catch (e) {
      print("Error deleting path: $e");
    }
  }

  Future<void> handleSelectPath(PathModel path) async {
    try {
      PathModel matchedPath = await apiService.getPathDetails(path.pathID);

      if (matchedPath.originID.isEmpty || matchedPath.destinationID.isEmpty) {
        print("Invalid path data");
        return;
      }

      SharedPreferences prefs = await SharedPreferences.getInstance();
      await prefs.setString('departure', jsonEncode(departure?.toJson()));
      await prefs.setString('arrival', jsonEncode(arrival?.toJson()));

      setState(() {
        departure = PlaceModel(
          id: matchedPath.originID,
          name: "Origin Name", // 실제 데이터를 매칭하여 설정
          latitude: 0.0, // 실제 데이터를 매칭하여 설정
          longitude: 0.0, // 실제 데이터를 매칭하여 설정
        );
        arrival = PlaceModel(
          id: matchedPath.destinationID,
          name: "Destination Name", // 실제 데이터를 매칭하여 설정
          latitude: 0.0, // 실제 데이터를 매칭하여 설정
          longitude: 0.0, // 실제 데이터를 매칭하여 설정
        );
        currentPath = matchedPath;
      });

      // Optionally navigate or perform additional actions
    } catch (e) {
      print("Error selecting path: $e");
    }
  }

  Future<void> handleItemDelete(String itemId) async {
    try {
      await apiService.deleteRecentHistory(itemId);
      setState(() {
        recentHistory.removeWhere((item) => item.id == itemId);
      });
      print("Deleted history item: $itemId");
    } catch (e) {
      print("Error deleting history item: $e");
    }
  }

  Future<void> handleSelectRecent(RecentHistoryItem item) async {
    try {
      PlaceModel matchedPlace = await apiService.getPlaceDetails(item.placeID);

      if (matchedPlace.id.isEmpty) {
        print("Place not found");
        return;
      }

      String placeType = 'arrival'; // Default or retrieve from state

      SharedPreferences prefs = await SharedPreferences.getInstance();

      if (placeType == 'departure') {
        await prefs.setString('departure', jsonEncode(matchedPlace.toJson()));
        setState(() {
          departure = matchedPlace;
        });
        print("Selected Departure: ${matchedPlace.name}");
      } else if (placeType == 'arrival') {
        await prefs.setString('arrival', jsonEncode(matchedPlace.toJson()));
        setState(() {
          arrival = matchedPlace;
        });
        print("Selected Arrival: ${matchedPlace.name}");
      } else {
        print("Invalid place type: $placeType");
      }

      // Optionally navigate or perform additional actions
    } catch (e) {
      print("Error selecting recent item: $e");
    }
  }

  void handleInputClick(String type) {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (context) => SearchPlacePage(placeType: type),
      ),
    ).then((selectedPlace) {
      if (selectedPlace != null && selectedPlace is PlaceWithType) {
        if (selectedPlace.placeType == 'departure') {
          setState(() {
            departure = selectedPlace.place;
          });
          SharedPreferences.getInstance().then((prefs) {
            prefs.setString(
                'departure', jsonEncode(selectedPlace.place.toJson()));
          });
        } else if (selectedPlace.placeType == 'arrival') {
          setState(() {
            arrival = selectedPlace.place;
          });
          SharedPreferences.getInstance().then((prefs) {
            prefs.setString(
                'arrival', jsonEncode(selectedPlace.place.toJson()));
          });
        }
      }
    });
  }

  Future<void> handleFindPath() async {
    if (departure == null || arrival == null) {
      print("Please select both departure and arrival locations.");
      return;
    }

    setState(() {
      isLoading = true;
    });

    try {
      // Add path to backend
      await apiService.addOrUpdatePath(
          userID, departure!.id, arrival!.id);

      // Fetch coordinates
      Coordinates startCoordinates =
          await apiService.getCoordinates(departure!.id);
      Coordinates goalCoordinates =
          await apiService.getCoordinates(arrival!.id);

      // Make search path request
      PathModel guide =
          await apiService.searchPath(startCoordinates, goalCoordinates);

      setState(() {
        currentPath = guide;
      });

      print("Guide Data: ${guide.toJson()}");

      // Optionally navigate or display guide data
    } catch (e) {
      print("Error finding path: $e");
    } finally {
      setState(() {
        isLoading = false;
      });
    }
  }

  Widget buildBody() {
    if (isLoading) {
      return Center(
        child: CircularProgressIndicator(),
      );
    }

    return SingleChildScrollView(
      padding: EdgeInsets.all(16.0),
      child: Column(
        children: [
          SearchBar(
            departure: departure?.name,
            arrival: arrival?.name,
            onDepartureClick: () => handleInputClick('departure'),
            onArrivalClick: () => handleInputClick('arrival'),
            onButtonClick: handleFindPath,
          ),
          ArrivalTime(
            arrivalTime: null, // 필요에 따라 구현
          ),
          Favorites(
            favorites: favorites,
            onFavoriteSelect: (fav) {
              // 즐겨찾기 선택 처리
            },
            onFavoriteDelete: (favId) {
              // 즐겨찾기 삭제 처리
            },
          ),
          RecentHistory(
            recentPaths: recentPaths,
            recentHistory: recentHistory,
            onPathDelete: handlePathDelete,
            onPathSelect: handleSelectPath,
            onItemDelete: handleItemDelete,
            onItemSelect: handleSelectRecent,
          ),
          if (currentPath != null) ...[
            SizedBox(height: 20),
            Text(
              "Path Details",
              style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
            ),
            ...currentPath!.segments.map((segment) => ListTile(
                  leading: CircleAvatar(
                    child: Text(segment.sequenceNumber.toString()),
                  ),
                  title: Text(segment.description),
                  subtitle: Text(
                      "Distance: ${segment.distance}m, Duration: ${segment.duration.inMinutes} mins, Bearing: ${segment.bearing ?? 'N/A'}°"),
                )),
          ],
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: Header(),
      body: buildBody(),
    );
  }
}
