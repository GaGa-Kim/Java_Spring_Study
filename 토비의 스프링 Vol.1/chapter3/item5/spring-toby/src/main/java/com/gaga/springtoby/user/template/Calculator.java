package com.gaga.springtoby.user.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Calculator 클래스 코드
 */
public class Calculator {

    // 템플릿 <<template>>
    // BufferedReaderCallback을 사용하는 템플릿 메소드
    public Integer fileReadTemplate(String filepath, BufferedReaderCallback callback) throws IOException {
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(filepath));
            // 콜백 오브젝트 호출을 통해 템플릿에서 만든 컨텍스트 정보인 BufferedReader를 전달해주고 콜백의 작업 결과를 받아둔다.
            int ret = callback.doSomethingWithReader(br);
            return ret;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    // 템플릿 <<template>>
    // LineCallback을 사용하는 템플릿 메소드 (타입 파라미터 추가)
    public <T> T lineReadTemplate(String filepath, LineCallback<T> callback, T initVal) throws IOException {
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(filepath));
            T res = initVal;
            String line = null;
            // 파일의 각 라인을 푸르를 돌면서 가져오는 것도 템플릿이 담당한다.
            while ((line = br.readLine()) != null) {
                // 각 라인의 내용을 가지고 계산하는 작업만 콜백에게 맡긴다.
                res = callback.doSomethingWithLine(line, res);
            }
            return res;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    // 클라이언트 <<client>>
    // fileReadTemplate 템플릿/콜백을 적용한 calSum() 메소드
    public Integer calSumWithReader(String filepath) throws IOException {
        // 콜백 <<callback>>
        BufferedReaderCallback sumCallback = new BufferedReaderCallback() {
            @Override
            public Integer doSomethingWithReader(BufferedReader br) throws IOException {
                Integer sum = 0;
                String line = null;
                while ((line = br.readLine()) != null) {
                    sum += Integer.valueOf(line);
                }
                return sum;
            }
        };
        return fileReadTemplate(filepath, sumCallback);
    }

    // 클라이언트 <<client>>
    // lineReadTemplate 템플릿/콜백을 적용한 calSum() 메소드
    public Integer calSumWithLine(String filepath) throws IOException {
        // 콜백 <<callback>>
        LineCallback<Integer> sumCallback = new LineCallback<Integer>() {
            @Override
            public Integer doSomethingWithLine(String line, Integer value) {
                return value + Integer.valueOf(line);
            }
        };
        return lineReadTemplate(filepath, sumCallback, 0);
    }

    // 클라이언트 <<client>>
    // fileReadTemplate 템플릿/콜백을 적용한 calMultiply() 메소드
    public Integer calMultiplyWithReader(String filepath) throws IOException {
        // 콜백 <<callback>>
        BufferedReaderCallback multiplyCallback = new BufferedReaderCallback() {
            @Override
            public Integer doSomethingWithReader(BufferedReader br) throws IOException {
                Integer multiply = 1;
                String line = null;
                while ((line = br.readLine()) != null) {
                    multiply *= Integer.valueOf(line);
                }
                return multiply;
            }
        };
        return fileReadTemplate(filepath, multiplyCallback);
    }

    // 클라이언트 <<client>>
    // lineReadTemplate 템플릿/콜백을 적용한 calMultiply() 메소드
    public Integer calMultiplyWithLine(String filepath) throws IOException {
        // 콜백 <<callback>>
        LineCallback<Integer> multiplyCallback = new LineCallback<Integer>() {
            @Override
            public Integer doSomethingWithLine(String line, Integer value) {
                return value + Integer.valueOf(line);
            }
        };
        return lineReadTemplate(filepath, multiplyCallback, 0);
    }

    // 클라이언트 <<client>>
    // lineReadTemplate 템플릿/콜백을 적용한 concatenate() 메소드
    public String concatenate(String filepath) throws IOException {
        // 콜백 <<callback>>
        LineCallback<String> concatenateCallback = new LineCallback<String>() {
            @Override
            public String doSomethingWithLine(String line, String value) {
                return value + line;
            }
        };
        return lineReadTemplate(filepath, concatenateCallback, "");
    }
}
