package com.project.bbibbi.domain.goodTip.tipBookMark.service;

import com.project.bbibbi.domain.goodTip.tipBookMark.entity.TipBookMark;
import com.project.bbibbi.domain.goodTip.tipBookMark.repository.TipBookMarkRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class TipBookMarkService {

    private final TipBookMarkRepository tipBookmarkRepository;

    public TipBookMarkService(TipBookMarkRepository tipBookmarkRepository) {
        this.tipBookmarkRepository = tipBookmarkRepository;
    }

    public TipBookMark settingTipBookmark(TipBookMark tipBookmark) {

        Integer existCount = tipBookmarkRepository.existCount(tipBookmark.getTip().getTipId(), tipBookmark.getMember().getMemberId());

        if (existCount == 0) {
            tipBookmarkRepository.save(tipBookmark);
        }
        else {
            tipBookmarkRepository.deleteByTipIdAndMemberId(tipBookmark.getTip().getTipId(), tipBookmark.getMember().getMemberId());
        }

        Integer updatedBookmarkCount = tipBookmarkRepository.existCount(tipBookmark.getTip().getTipId(), tipBookmark.getMember().getMemberId());

        TipBookMark updatedTipBookmark = new TipBookMark();
        updatedTipBookmark.setTip(tipBookmark.getTip());
        updatedTipBookmark.setMember(tipBookmark.getMember());

        if (updatedBookmarkCount == 0) {
            updatedTipBookmark.setBookmarkYn(false);
        }
        else {
            updatedTipBookmark.setBookmarkYn(true);
        }

        updatedTipBookmark.setBookmarkCount(tipBookmarkRepository.tipBookmarkCount(tipBookmark.getTip().getTipId()));

        return updatedTipBookmark;
    }
}
