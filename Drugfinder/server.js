var express    = require('express'); 			//requires express framework
var app        = express(); 					//initializes express framework
var bodyParser = require('body-parser');		//used for parsing JSON

var mongoose   = require('mongoose');			//ORM used for MongoDB

var Drug	   = require('./models/drug');		//location of drug model
var Pharmacy   = require('./models/pharmacy'); 	//location of pharmacy model

console.log("proslo1");
mongoose.connect('mongodb://tito:tito@linus.mongohq.com:10080/app30593667');	//connection string for MongoDB

app.use(bodyParser.urlencoded({ extended: true }));	//allows extended character set 
app.use(bodyParser.json());							//readies app for JSON
console.log("proslo2");
var port = process.env.PORT || 8080; 		//sets port
// var port = 8080; 		//sets port
		console.log(port);

var router = express.Router(); 				//gets the express router instance
console.log("proslo3");
//main route for gathering and creation of drugs
router.route('/drugs')

	//POST /drugs
	.post(function(req, res) {
		var drug = new Drug(); 		

		drug.name = req.body.name;
		drug.directions = req.body.directions;
		drug.quantity = req.body.quantity;
		drug.requiresPerscription = req.body.requiresPerscription;
		drug.priceRange = req.body.priceRange;
		drug.priceRange = req.body.priceRange;
		drug.ingredients = req.body.ingredients;
		drug.equivalents = req.body.equivalents.split(',');
		drug.pharmacies = req.body.pharmacies.split(',');


		drug.save(function(err) {
			if (err)
				res.send(err);

			res.jsonp({ message: 'Drug added!' });
		});	
	})

	//GET /drugs
	.get(function(req, res) {
		Drug.find(function(err, drugs) {
			if (err)
				res.send(err);

			res.jsonp(drugs);	//retrieves all drugs from the database
		});
	});

router.route('/pharmacies')

	//POST /drugs
	.post(function(req, res) {
		
		var pharmacy = new Pharmacy(); 		

		pharmacy.name = req.body.name;
		pharmacy.address = req.body.address;
		pharmacy.xcoords = req.body.xcoords;
		pharmacy.ycoords = req.body.ycoords;
		pharmacy.takesPerscriptions = req.body.takesPerscriptions;
		pharmacy.phones = req.body.phones;
		pharmacy.workingTime = req.body.workingTime;
		pharmacy.county = req.body.county;
		pharmacy.drugs = req.body.drugs.split(',');

		pharmacy.save(function(err) {
			if (err)
				res.send(err);

			res.jsonp({ message: 'Pharmacy added!' });
		});	
	})

	//GET /drugs
	.get(function(req, res) {
		Pharmacy.find(function(err, pharmacies) {
			if (err)
				res.send(err);

			res.jsonp(pharmacies);	//retrieves all drugs from the database
		});
	});

router.route('/drugs/:drug_id')
	//GET /drugs/name/:drug_name
	.get(function(req, res) {
		Drug.findById(req.params.drug_id, function(err, drug) {
			if (err)
				res.send(err);
			res.jsonp(drug);		//retrieves all instances of the drug with the given id
		})
	})
	.patch(function(req, res) {

		Drug.findById(req.params.drug_id, function(err, drug) {

			if (err)
				res.send(err);

			if(req.body.name !== undefined && req.body.name !== null)
				drug.name = req.body.name;
			
			if(req.body.directions !== undefined && req.body.directions !== null)
				drug.directions = req.body.directions;
			
			if(req.body.quantity !== undefined && req.body.quantity !== null)
				drug.quantity = req.body.quantity;
			
			if(req.body.requiresPerscription !== undefined && req.body.requiresPerscription !== null)
				drug.requiresPerscription = req.body.requiresPerscription;
			
			if(req.body.priceRange !== undefined && req.body.priceRange !== null)
				drug.priceRange = req.body.priceRange;
			
			if(req.body.ingredients !== undefined && req.body.ingredients !== null)
				drug.ingredients = req.body.ingredients;

			if(req.body.equivalents !== undefined && req.body.equivalents !== null)
				drug.equivalents = req.body.equivalents.split(',');
			
			if(req.body.pharmacies !== undefined && req.body.pharmacies !== null)
				drug.pharmacies = req.body.pharmacies.split(',');

			drug.save(function(err) {
				if (err)
					res.send(err);

				res.jsonp({ message: 'Drug updated!' });
			});

		});
	})
	.delete(function(req, res) {
		Drug.remove({ _id: req.params.drug_id }, function(err, drug) {
			if (err)
				res.send(err);

			res.jsonp({ message: 'Successfully deleted drug' });
		})
	});

router.route('/pharmacies/:pharmacy_id')
	//GET /drugs/name/:drug_name
	.get(function(req, res) {
		Pharmacy.findById(req.params.pharmacy_id, function(err, pharmacy) {
			if (err)
				res.send(err);
			res.jsonp(pharmacy);		//retrieves all instances of the pharmacy with the given id
		});
	})
	.patch(function(req, res) {

		Pharmacy.findById(req.params.drug_id, function(err, pharmacy) {

			if (err)
				res.send(err);

			if(req.body.name !== undefined && req.body.name !== null)
				pharmacy.name = req.body.name;
			
			if(req.body.address !== undefined && req.body.address !== null)
				pharmacy.address = req.body.address;
			
			if(req.body.xcoords !== undefined && req.body.xcoords !== null)
				pharmacy.xcoords = req.body.xcoords;
			
			if(req.body.ycoords !== undefined && req.body.ycoords !== null)
				pharmacy.ycoords = req.body.ycoords;
			
			if(req.body.takesPerscriptions !== undefined && req.body.takesPerscriptions !== null)
				pharmacy.takesPerscriptions = req.body.takesPerscriptions;
			
			if(req.body.phones !== undefined && req.body.phones !== null)
				pharmacy.phones = req.body.phones;
			
			if(req.body.workingTime !== undefined && req.body.workingTime !== null)
				pharmacy.workingTime = req.body.workingTime;
			
			if(req.body.county !== undefined && req.body.county !== null)
				pharmacy.county = req.body.county;

			if(req.body.drugs !== undefined && req.body.drugs !== null)
				pharmacy.drugs = req.body.drugs.split(',');

			pharmacy.save(function(err) {
				if (err)
					res.send(err);

				res.jsonp({ message: 'Pharmacy updated!' });
			});

		});
	})
	.delete(function(req, res) {
		Pharmacy.remove({ _id: req.params.pharmacy_id }, function(err, pharmacy) {
			if (err)
				res.send(err);

			res.jsonp({ message: 'Successfully deleted drug' });
		})
	});

app.use('/', router);

app.listen(port);
