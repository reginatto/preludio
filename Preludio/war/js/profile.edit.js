
function profileEditLocationInitialize() {

	var locationInputId = getLocationInputId();
	var locationInput = document.getElementById(locationInputId);
	var locationAutocomplete = new google.maps.places.Autocomplete(locationInput);

	locationAutocomplete.setTypes( [ '(cities)' ]);

	var locationInfoWindow = new google.maps.InfoWindow();

	google.maps.event.addListener(locationAutocomplete, 'place_changed', function() {
		locationInfoWindow.close();
		locationInput.className = '';
		var place = locationAutocomplete.getPlace();
		if (!place.geometry) {
			locationInput.className = 'notfound';
			return;
		}

		var address = '';
		if (place.address_components) {
			address = [
					(place.address_components[0]
							&& place.address_components[0].short_name || ''),
					(place.address_components[1]
							&& place.address_components[1].short_name || ''),
					(place.address_components[2]
							&& place.address_components[2].short_name || '') ]
					.join(' ');
		}

		locationInfoWindow.setContent('<div><strong>' + place.name + '</strong><br>' + address);
	});
}
