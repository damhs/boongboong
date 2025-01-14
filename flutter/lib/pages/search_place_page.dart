// lib/pages/search_place_page.dart
import 'package:flutter/material.dart';
import '../models/place_model.dart';

class SearchPlacePage extends StatefulWidget {
  final String placeType; // 'departure' 또는 'arrival'

  const SearchPlacePage({Key? key, required this.placeType}) : super(key: key);

  @override
  _SearchPlacePageState createState() => _SearchPlacePageState();
}

class _SearchPlacePageState extends State<SearchPlacePage> {
  List<PlaceModel> allPlaces = [
    // 실제 데이터로 대체
    PlaceModel(id: '1', name: 'Seoul Station', latitude: 37.5665, longitude: 126.9780),
    PlaceModel(id: '2', name: 'Busan Station', latitude: 35.1796, longitude: 129.0756),
    // 추가 장소들...
  ];

  List<PlaceModel> filteredPlaces = [];

  @override
  void initState() {
    super.initState();
    filteredPlaces = allPlaces;
  }

  void searchPlaces(String query) {
    final results = allPlaces
        .where((place) =>
            place.name.toLowerCase().contains(query.toLowerCase()))
        .toList();

    setState(() {
      filteredPlaces = results;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Select ${widget.placeType.capitalize()}'),
      ),
      body: Column(
        children: [
          Padding(
            padding: EdgeInsets.all(8.0),
            child: TextField(
              decoration: InputDecoration(
                labelText: 'Search Place',
                border: OutlineInputBorder(),
                prefixIcon: Icon(Icons.search),
              ),
              onChanged: searchPlaces,
            ),
          ),
          Expanded(
            child: ListView.builder(
              itemCount: filteredPlaces.length,
              itemBuilder: (context, index) {
                final place = filteredPlaces[index];
                return ListTile(
                  title: Text(place.name),
                  onTap: () {
                    Navigator.pop(
                        context,
                        PlaceWithType(
                            place: place, placeType: widget.placeType));
                  },
                );
              },
            ),
          ),
        ],
      ),
    );
  }
}

// 확장 메서드: 문자열의 첫 글자 대문자화
extension StringCasingExtension on String {
  String capitalize() =>
      this.length > 0 ? '${this[0].toUpperCase()}${this.substring(1)}' : '';
}

// PlaceWithType 클래스: 선택된 장소와 그 유형을 함께 반환
class PlaceWithType {
  final PlaceModel place;
  final String placeType; // 'departure' 또는 'arrival'

  PlaceWithType({required this.place, required this.placeType});
}
