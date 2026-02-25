$version = '4.10.9'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'F63B0D316A92A966B93DBA11012BE62BB3F4BFC4B94156322A1B6FECF0B9BD68'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
