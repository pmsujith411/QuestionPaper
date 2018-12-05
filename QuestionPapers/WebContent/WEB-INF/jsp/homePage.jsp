<!DOCTYPE html>
<html lang="en">
<head>
  <title>Question Paper</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>
</head>
<body onload="displayMessage()">
<br>

<div class="container">
  <h2 align="center">QuestionPaper Creator</h2>
  <br>
  <!-- Nav pills -->
  <ul class="nav nav-tabs" id="myTab">
    <li class="nav-item">
      <a class="nav-link active" data-toggle="tab" href="#addQuestion">Add Question</a>
    </li>
    <li class="nav-item">
      <a class="nav-link" data-toggle="tab" href="#createQuestionPaper">Create QuestionPaper</a>
    </li>
    <li class="nav-item">
      <a class="nav-link" data-toggle="tab" href="#deleteQuestion">Delete Question</a>	
    </li>
  </ul>

  <!-- Tab panes -->
  <div class="tab-content">
    <div id="addQuestion" class="container tab-pane active"><br>
      	<form action="addQuestion">
			<div class="container">
			<h3>Add Question</h3><br>
    		<div class="row">
      			<div class="col-sm-2">Enter Question:</div>
      			<div class="col-sm-4"><textarea class="form-control" name="question" cols="100" required="required"></textarea></div>
    		</div>
    		<br>
    
    		<div class="row">
      			<div class="col-sm-2">Enter Options:</div>
      			<div class="col-sm-2"><input type="text" class="form-control" name="option1" required="required"></div>
      			<div class="col-sm-2"><input type="text" class="form-control" name="option2" required="required"></div>
      			<div class="col-sm-2"><input type="text" class="form-control" name="option3"></div>
      			<div class="col-sm-2"><input type="text" class="form-control" name="option4"></div>
    		</div>
    		<br>

		    <div class="row">
    			<div class="col-sm-2">Enter Answer:</div>
      			<div class="col-sm-2">
      				<select class="form-control" name="answer" required="required">
      					<option style="display:none"></option>
        				<option value="1">a</option>
        				<option value="2">b</option>
        				<option value="3">c</option>
        				<option value="4">d</option>
      				</select>
      			</div>
    		</div>
    		<br>
	
			<div class="row">
				<div class="col-sm-2"></div>
				<div class="col-sm-2"><button type="submit" class="btn btn-success">submit</button></div>
			</div>
	
			<div class="row">
    			<div class="col-sm-2"></div>
				<div class="col-sm-4"><p class="text-success" style="display:none" id="successMsg">${successMsg}</p></div>
    		</div>
    			
    		<div class="row">
    			<div class="col-sm-2"></div>
    			<div class="col-sm-4"><p class="text-danger" style="display:none" id="errorMsg">${errorMsg}</p></div>
    		</div>
  		</div>
	</form>
  </div>
  
  <div id="createQuestionPaper" class="container tab-pane fade"><br>
      	<form action="genQuestion" id="myForm">		
			<div class="container">
				<h3>Create QuestionPaper</h3><br>
				
    			<div class="row">
      				<div class="col-sm-2">Enter the Count:</div>
      				<div class="col-sm-4"><input type="text" name="count" class="form-control" required="required"></div>
    			</div>
    			<br>
    
    			<div class="row">
    				<div class="col-sm-2"></div>
      				<div class="col-sm-2"><button type="submit" class="btn btn-success">submit</button></div>
      			</div>
      			
      			<div class="row">
    				<div class="col-sm-2"></div>
					<div class="col-sm-4"><p class="text-success" style="display:none" id="successMsg2">${successMsg2}</p></div>
      			</div>
      			
      			<div class="row">
    				<div class="col-sm-2"></div>
      				<div class="col-sm-4"><p class="text-danger" style="display:none" id="errorMsg2">${errorMsg2}</p></div>
      			</div>
      			
      		</div>
		</form>
    </div>
    
    <div id="deleteQuestion" class="container tab-pane fade"><br>
    	<form action="deleteQuestion" id="myForm">
    		<div class="container">
    			<h3>Delete Question</h3><br>
    			
    			<div class="row">
    				<div class="col-sm-2">Enter the Question:</div>
    				<div class="col-sm-4"><input type="text" name="question" class="form-control" required="required"></div>
    			</div><br>
    			
    			<div class="row">
    				<div class="col-sm-2"></div>
    				<div class="col-sm-2"><button type="submit" class="btn btn-success">submit</button></div>
    			</div>
    			
    			<div class="row">
    				<div class="col-sm-2"></div>
					<div class="col-sm-4"><p class="text-success" style="display:none" id="successMsg3">${successMsg3}</p></div>
      			</div>
      			
      			<div class="row">
    				<div class="col-sm-2"></div>
      				<div class="col-sm-4"><p class="text-danger" style="display:none" id="errorMsg3">${errorMsg3}</p></div>
      			</div>
      			
    			<br>
    		</div>
    	</form>
    </div>
  </div>
</div>

<script type="text/javascript">

	function displayMessage() {
		document.getElementById("myForm").reset();
		document.getElementById('errorMsg').style.display = 'block';
       	document.getElementById('successMsg').style.display = 'block';
       	document.getElementById('errorMsg2').style.display = 'block';
       	document.getElementById('successMsg2').style.display = 'block';
       	document.getElementById('errorMsg3').style.display = 'block';
       	document.getElementById('successMsg3').style.display = 'block';
   	}

	$(document).ready(function(){
	    $('a[data-toggle="tab"]').on('show.bs.tab', function(e) {
	        localStorage.setItem('activeTab', $(e.target).attr('href'));
	    });
	    var activeTab = localStorage.getItem('activeTab');
	    if(activeTab){
	        $('#myTab a[href="' + activeTab + '"]').tab('show');
	    }
	});
	
</script>

</body>
</html>
