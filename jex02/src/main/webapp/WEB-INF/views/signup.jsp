<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@include file="../views/includes/header.jsp" %>

				
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">회원가입</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-7">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            
                        </div>
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-lg-7">
                                    <form role="form" action="/signup" method="post">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
									<input type='hidden' id='pageNum' name='pageNum' value='<c:out value="${cri.pageNum }"/>'>	
                                        <!-- 
                                         <div class="form-group">
                                            <label>Text Input</label>
                                            <input class="form-control">
                                            <p class="help-block">Example block-level help text here.</p>
                                        </div>	
                                        -->
                                        <div><label>UserID</label></div>
                                        <div class="input-group">
                                            <input class="form-control" name="userid" id="userid" placeholder="사용하실 ID를 입력하세요.">
                                        <span class="input-group-btn">
											<button class="btn btn-info" data-oper="idChk" type="button">중복체크</button>
	     									 </span>
                                        </div>
                                        <div class="form-group">
                                            <label>UserName</label>
                                            <input class="form-control" name="username" id="username" placeholder="사용하실 이름을 입력하세요.">
                                        </div>
                                        <div class="form-group">
                                            <label>Password</label>
                                            <input type="password" class="form-control" id="userpw" name="userpw" placeholder="비밀번호를 입력하세요">
                                            <p class="help-block">4자리 이상의 비밀번호를 설정해주세요.</p>
                                        </div>
                                        <div class="form-group">
                                            <label>Confirm Password</label>
                                            <input type="password" class="form-control" id="usercpw" name="usercpw" placeholder="비밀번호 확인">
                                        </div>
                                        <button type="submit" data-oper="submit" class="btn btn-primary">Submit Button</button>
                                        <button type="button" data-oper="back" class="btn btn-danger">취소</button>
                                        </form>
                                </div>
                                <!-- /.col-lg-6 (nested) -->
                            </div>
                            <!-- /.row (nested) -->
                        </div>
                        <!-- /.panel-body -->
                    </div>
                    <!-- /.panel -->
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
        </div>
        <script type="text/javascript">
        
    	var csrfHeaderName="${_csrf.headerName}";
    	var csrfTokenValue="${_csrf.token}";
    	var chk=false;
    	var btnOpe;
    	var Id;
		var formObj= $("form[role='form']");
        $(document).ready(function(){
            $('button').on("click",function(e){
            	e.preventDefault();
            	btnOper=$(this).data("oper");
            	if(btnOper==='idChk'){
            		console.log('중복체크');
            		idcheck();
            	}
            	else if(btnOper==='submit'){
            		console.log('회원가입');
            		//idcheck();
            		signup();
            	}
            	else if(btnOper==='back'){
            		console.log('취소');
            		self.location='http://localhost:8088/'
            	}
            })   
        })
        function idcheck(){
        	id=$("#userid").val();
        	console.log(id.length)
        	if(id==''){
        		alert("아이디를 입력하세요");
        		return;
        	}

        	if(checkSpace(id)||checkSpecial(id)){
        		alert("특수문자 혹은 공백문자가 포함된 아이디는 사용할 수 없습니다.");
        		return;
        	}
        	
        	if(id.length<3){
        		alert("최소 세자리 이상의 아이디를 입력해주세요.");
        		return;
        	}

        	
        	console.log(id);
        	$.ajax({
        	    url: '/idchek',
        	    type: 'POST',
        	    data: {"userid": id},
        	    async: false,
        		beforeSend: function(xhr){
        			xhr.setRequestHeader(csrfHeaderName,csrfTokenValue)
        		},
        	    success: function (data) {
        	           if(data==''){
        	        	   alert("사용가능한 아이디 입니다.");
                   		   chk=true;
        	           }
        	           else{
        	        	   alert("사용할 수 없는 아이디 입니다.");
        	        	   chk=false;
        	           }
        	        }
        	});
        	
        }
        function signup(){
    		var Hid=$("#userid").val();
    		if($("#userid").val()==''){
    			alert("아이디를 입력해주세요.");
    			$("#userid").focus();
    			return false;
    		}
    		if($("#username").val()==''){
    			alert("회원명을 입력해주세요.");
    			$("#username").focus();
    			return false;
    		}
    		if($("#userpw").val()==''){
    			alert("비밀번호를 입력해주세요.");
    			$("#userpw").focus();
    			return false;
    		}
        	if(chk==false){

        		alert("아이디 중복을 확인해주세요.");
        		return;
        	}
        	else{
        		if(Hid!=id){
        			alert("아이디 중복을 확인해주세요.");
        			id='';
        			return;
        		}
        		else{
        			if($("#userpw").val()!=$("#usercpw").val()){
        				alert("비밀번호가 일치하지 않습니다.");
        				return;
        			}
        			formObj.submit();
        		}
        	}
        }
        function checkSpace(str) { 
        	if(str.search(/\s/) != -1) { 
        		return true;
        		} 
        	else { 
        		return false; 
        		} 
        	} // 특수 문자가 있나 없나 체크 
        function checkSpecial(str) {
        	var special_pattern = /[`~!@#$%^&*|\\\'\";:\/?]/gi; 
        	if(special_pattern.test(str) == true) {
        		return true; 
        		} 
        	else {
        		return false; 
        		} 
        	} // 비밀번호 패턴 체크 (8자 이상, 문자, 숫자, 특수문자 포함여부 체크) 
        function checkPasswordPattern(str) {
        		var pattern1 = /[0-9]/;// 숫자 
        		var pattern2 = /[a-zA-Z]/; // 문자 
        		var pattern3 = /[~!@#$%^&*()_+|<>?:{}]/; // 특수문자 
        		if(!pattern1.test(str) || !pattern2.test(str) || !pattern3.test(str) || str.length < 8) {
        			alert("비밀번호는 8자리 이상 문자, 숫자, 특수문자로 구성하여야 합니다.");
        			return false; 
        			}
        		else { 
        			return true; 
        			} 
        		}

        

        </script>
<%@ include file="../views/includes/footer.jsp"%>