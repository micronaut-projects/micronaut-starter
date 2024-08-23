$version = '4.6.0'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'C191D75D51AADE4F73F38BEA8E242BF59BA848C2BEF2FC38F453A4AEDB46330C'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
