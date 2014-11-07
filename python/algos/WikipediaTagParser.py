"""
   Naive parser which extracts infobox from wikipedia text
"""

import re
import traceback

rx = re.compile("{{Infobox", re.MULTILINE|re.DOTALL|re.VERBOSE)

def extractInfobox(text):
  """
  """
  #Trim the chars before the starting tag {{MyInfobox
  rxs = rx.search(text)
  if not rxs:
    return ""

  startingPos = rxs.start()
  text = text[startingPos:]

  #Let's remove the tail of the string after "}}"
  closTagStr = "}}"
  openTagStr = "{{"
  lastClosTagFound = 2
  lastOpenTagFound = 0

  while True:
        lastClosTagFound = text.find(closTagStr, lastClosTagFound) # NO, we cannot use rfind!! But, is this the last? Let's check
        subText = text[:lastClosTagFound + len(closTagStr)]
        lastOpenTagFound = subText.find(openTagStr, lastOpenTagFound + len(closTagStr))
        if lastOpenTagFound == -1:
                return subText

        lastClosTagFound +=2

def test():
  try:
        t= "some crap there\n {{Muusikko gigi {{come cavolo}} test}} some crap there"
        tout = extractInfobox(t)
        ttest = "{{Muusikko gigi {{come cavolo}} test}}"
        assert(tout == ttest)

        t= "some crap there\n {{Muusikko gigi {{come cavolo}} {{something}} test}} some crap there"
        tout = extractInfobox(t)
        ttest = "{{Muusikko gigi {{come cavolo}} {{something}} test}}"
        assert(tout == ttest)

        t= "some crap there\n {{Muusikko gigi {{{{come cavolo}} {{something}}}} test}} some crap there"
        tout = extractInfobox(t)
        ttest = "{{Muusikko gigi {{{{come cavolo}} {{something}}}} test}}"
        assert(tout == ttest)
  except:
          print "TEXT: " + tout
          traceback.print_exc()

  print "All tests passed"
