$version = '4.1.3'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'B51AC75297435C4D9DB9AF9AF72240C406B9AACB4C06277714B3220998F5BB63'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
