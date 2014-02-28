$('#query_string').typeahead({
	source: function (query, process) {
		return $.get('/qsearch/typeahead', { query_string : query }, function (data) {
			if (typeof data == 'string') data = JSON.parse(data)
			process(data)
		});
	},

	matcher: function (item) {
		// trust the ajax service results
		return true;
	}
});
