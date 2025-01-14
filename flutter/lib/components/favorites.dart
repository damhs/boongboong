// lib/components/favorites.dart
import 'package:flutter/material.dart';
import '../models/favorite.dart';

class Favorites extends StatelessWidget {
  final List<Favorite> favorites;
  final Function(Favorite) onFavoriteSelect;
  final Function(String) onFavoriteDelete;

  const Favorites({
    Key? key,
    required this.favorites,
    required this.onFavoriteSelect,
    required this.onFavoriteDelete,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return ExpansionTile(
      title: Text('Favorites'),
      children: favorites.map((fav) {
        return ListTile(
          title: Text('${fav.departure} → ${fav.arrival}'),
          trailing: IconButton(
            icon: Icon(Icons.delete, color: Colors.red),
            onPressed: () => onFavoriteDelete(fav.id),
          ),
          onTap: () => onFavoriteSelect(fav),
        );
      }).toList(),
    );
  }
}
