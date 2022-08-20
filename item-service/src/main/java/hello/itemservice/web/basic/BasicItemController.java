package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model){
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm(){
        return "/basic/addForm";
    }

//    @PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                       @RequestParam int price,
                       @RequestParam Integer quantity,
                       Model model){

        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        model.addAttribute("item", item);
        return "basic/addForm";
    }

//    @PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item/*, Model model*/){
        // @ModelAttribute에서 추가해준 이름으로 model에 매핑된다.
        // 1. 요청 파라미터 처리
        // 2. model에 attribute 추

        itemRepository.save(item);
//        model.addAttribute("item", item);
        // 따라서 생략해도 상관없다.

        return "basic/addForm";
    }

//    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item){
        // 변수의 객체의 맨 앞을 소문자로 바꾼것이 model의 attribute에 넣어준다.

        itemRepository.save(item);
//        model.addAttribute("item", item);

        return "basic/addForm";
    }

//    @PostMapping("/add")
    public String addItemV4(Item item){
        // @ModelAttribute를 생략할수도 있다.
        itemRepository.save(item);

        return "basic/addForm";
    }

//    @PostMapping("/add")
    public String addItemV5(Item item){
        itemRepository.save(item);

        return "redirect:/basic/items/" + item.getId();
    }

    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes){
        Item saveItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", saveItem.getId());
        redirectAttributes.addAttribute("status", true);

        return "redirect:/basic/items/{itemId}";
    }


    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }


    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item){
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}";
    }


    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init(){
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }
}
