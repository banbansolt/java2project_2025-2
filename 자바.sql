
CREATE TABLE "부서" (
    "부서ID" NUMBER PRIMARY KEY,
    "부서명" VARCHAR2(50) NOT NULL UNIQUE,  
    "위치" VARCHAR2(100)                    
);
/

-- 2. 의사 (DOCTOR) 테이블 생성
CREATE TABLE "의사" (
    "면허번호" VARCHAR2(50) PRIMARY KEY, 
    "이름" VARCHAR2(20) NOT NULL,
    "연락처" NUMBER, 
    "부서ID" NUMBER NOT NULL,  
    FOREIGN KEY ("부서ID") REFERENCES "부서"("부서ID")
);
/

-- 3. 환자 테이블 생성
CREATE TABLE "환자" (
    "정보" VARCHAR2(20) PRIMARY KEY,       
    "이름" VARCHAR2(50) NOT NULL,
    "생년월일" DATE NOT NULL,
    "주소" VARCHAR2(100)
);
/

-- 4. 약국 테이블 생성 
CREATE TABLE "약국" (
    "약국ID" NUMBER PRIMARY KEY,
    "약국명" VARCHAR2(50) NOT NULL,
    "주소" VARCHAR2(100) NOT NULL,
    "연락처" NUMBER 
);
/

-- 5. 약품 테이블 생성
CREATE TABLE "약품" (
    "약품코드" VARCHAR2(10) PRIMARY KEY,  
    "약품명" VARCHAR2(100) NOT NULL,
    "제조사" VARCHAR2(50) NULL,
    "단위가격" NUMBER NOT NULL
);
/

-- 6. 진료  테이블 생성
CREATE TABLE "진료" (
    "진료ID" NUMBER PRIMARY KEY,
    "환자정보" VARCHAR2(20) NOT NULL,  
    "의사면허번호" VARCHAR2(50) NULL,   
    "진료일시" DATE NOT NULL,
    "진단명" VARCHAR2(100) NOT NULL,    
    FOREIGN KEY ("환자정보") REFERENCES "환자"("정보"),
    FOREIGN KEY ("의사면허번호") REFERENCES "의사"("면허번호")
);
/

-- 7. 처방전 테이블 생성 
CREATE TABLE "처방전" (
    "처방전ID" NUMBER PRIMARY KEY,
    "진료ID" NUMBER NOT NULL UNIQUE, 
    "약국ID" NUMBER,                 
    "발행일" DATE NOT NULL,
    "이행상태" VARCHAR2(10) DEFAULT '대기' NOT NULL, 
    FOREIGN KEY ("진료ID") REFERENCES "진료"("진료ID"),
    FOREIGN KEY ("약국ID") REFERENCES "약국"("약국ID")
);
/

-- 8. 처방상세 테이블 생성
CREATE TABLE "처방상세" (
    "처방전ID" NUMBER NOT NULL,
    "약품코드" VARCHAR2(10) NOT NULL,
    "용량" VARCHAR2(50) NOT NULL, 
    "수량" NUMBER NOT NULL,     
    PRIMARY KEY ("처방전ID", "약품코드"), 
    FOREIGN KEY ("처방전ID") REFERENCES "처방전"("처방전ID"),
    FOREIGN KEY ("약품코드") REFERENCES "약품"("약품코드")
);