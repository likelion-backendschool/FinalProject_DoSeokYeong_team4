# [1Week] 도석영
***

## 미션 요구사항 분석 & 체크리스트
### 필수과제
#### 회원 관련
- [x] 회원 가입
- [x] 로그인
- [x] 로그아웃
- [x] 회원정보수정
- [x] 아이디찾기

#### 글 CRUD
- [x] 글 작성
- [x] 글 수정
- [x] 글 리스트
- [x] 글 삭제

### 추가과제
- [x] 비번찾기
- [ ] 상품 등록
- [ ] 상품 수정
- [ ] 상품 리스트
- [ ] 상품 상세페이지

## 1주차 미션 요약
### 접근 방법
- 작성자나 접근권한에 대한 관리가 필요한 요구사항이기 때문에 회원 -> 글 순서로 진행하였습니다.
- 대부분은 스스로 만들었기 때문에 코드 완성도면에서 떨어질 수 있지만 최대한 직접 구현하고 진행이 막힐 때만 예전 코드를 참고했습니다.
- 수업시간에 주로 했봤던 내용들이거나 저번 아이디어톤에서도 해봤던 작업들 이었기 때문에 블로그보다는 예전 프로젝트 폴더들을 찾아봤습니다.
  - QueryDsl(https://github.com/jhs512/sb_qsl)
  - 해시태그(https://github.com/jhs512/sb_exam_2022_09_05__app10)
  - Toast Editor, layout(https://github.com/likelion-backendschool/Your_little_worries)
- 메일 서비스는 이번에 처음 해봤기 때문에 해당 블로그(https://kitty-geno.tistory.com/43) 참고했습니다.

**진행 순서**
1. **회원가입 및 회원가입 이메일 발송**
- `validation`과 html에서도 required 이용하여 이중으로 공백 확인
- Unique 어노테이션을 이용하여 중복된 username을 사용한 회원 가입 방지
  ```
  @PostMapping("/join")
    public String doJoin(@Valid MemberCreateForm memberCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "member/join";
        }
        if (!memberCreateForm.getPassword().equals(memberCreateForm.getPasswordConfirm())) {
            return "member/join";
        }

        if (memberCreateForm.getNickname().equals("") || memberCreateForm.getNickname().trim().length() < 1) { // 닉네임 설정 안한 경우 권한 레벨 3
            memberService.join(memberCreateForm.getUsername(), memberCreateForm.getPassword(), memberCreateForm.getEmail());
        } else {
            memberService.join(memberCreateForm.getUsername(), memberCreateForm.getPassword(), memberCreateForm.getNickname(),
                    memberCreateForm.getEmail());
        }
        mailService.sendMail(memberCreateForm.getEmail(), "회원 가입을 축하합니다",
                memberCreateForm.getUsername() + "님의 회원 가입을 축하합니다");

        return "redirect:/msg=joinSuccess";
    }
 ```
 - 닉네임 등록 유무에 따라 다른 권한을 부여하고 mailService를 통해 가입 축하 메일을 

### 특이사항
