<%
        //response.setHeader("Content-Type","application/force-download");
        response.setHeader("Content-Type","application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Content-Disposition","attachment;filename=export.xls");
        String xls = request.getParameter("exportContent");
        //xls = new String(xls.getBytes("iso-8859-1"),"UTF-8");
        System.out.println(xls);
        out.print(xls);
%>